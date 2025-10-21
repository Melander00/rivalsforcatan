
// export function replaceTemplate(data: any, template: string): string {
//     return template.replace(/\{([^{}]+)\}/g, (_, path) => {
//         return path.split('.').reduce((obj: any, key: any) => obj[key], data)
//     })
// }

import { GetCardInfo, GetResourceInfo } from "../resources/ResourceHandler";
import { ResourceAmount } from "../types/resource/resource";
import { Color } from "./Color";

export function handleTemplate(data: any, field: string | {template: string}, identifier = ""): string {
    if(typeof field === "string") return field;

    const template = field.template;
    if(template === undefined || template === null || typeof template !== "string") throw new Error(`Invalid description template of ${identifier}. Typeof: ${typeof template}`)

    return replaceTemplate(data, template);
}


const helpers: {
    [key: string]: (val: any) => string
} = {
    lower: (val) => val.toLocaleLowerCase(),
    upper: (val) => val.toLocaleUpperCase(),
    
    resource: (val) => handleTemplate({}, GetResourceInfo(val).name),
    iterateResources: (val: ResourceAmount[]): string => {
        return val.map(res => `${res.amount} ${helpers.resource(res.resourceType)}`).join(", ")
    },
    
    card: (val) => GetCardInfo(val).name,

    red: Color.red,
    green: Color.green,
    orange: Color.orange,
    brown: Color.brown,
    gold: Color.gold,
    yellow: Color.yellow,
    gray: Color.gray,
    blue: Color.blue,
}










export function replaceTemplate(data: any, template: string): string {
  function evaluate(str: string): string {
    // Find innermost expression (recursively)
    // It will match the *deepest* {...} first
    const regex =
      /\$([a-zA-Z_$][\w$]*)\{([^{}]+)\}|\{([^{}]+)\}/;

    const match = regex.exec(str);
    if (!match) return str;

    const [fullMatch, helperName, helperExpr, simpleExpr] = match;
    let value: any;

    if (helperName && helperExpr !== undefined) {
      // Recursively evaluate the inside of helperExpr
      const innerEvaluated = evaluate(helperExpr.trim());

      // Handle quoted literals or variable references
      const literalMatch = innerEvaluated.match(/^(['"])(.*?)\1$/);
      let resolvedInner: any;
      if (literalMatch) {
        resolvedInner = literalMatch[2];
      } else {
        // Try resolving path in data
        resolvedInner = resolvePath(data, innerEvaluated.trim());
        // If not found, just use the evaluated string itself
        if (resolvedInner === undefined) resolvedInner = innerEvaluated;
      }

      if (!(helperName in helpers))
        throw new Error(`Unknown helper: ${helperName}`);

      value = helpers[helperName](resolvedInner);
    } else if (simpleExpr !== undefined) {
      // Recursively evaluate simpleExpr as well
      const innerEvaluated = evaluate(simpleExpr.trim());

      const literalMatch = innerEvaluated.match(/^(['"])(.*?)\1$/);
      let resolved: any;
      if (literalMatch) {
        resolved = literalMatch[2];
      } else {
        resolved = resolvePath(data, innerEvaluated.trim());
        if (resolved === undefined) resolved = innerEvaluated;
      }

      value =
        typeof resolved === "object"
          ? JSON.stringify(resolved)
          : resolved;
    }

    // Replace and continue evaluating
    return evaluate(str.replace(fullMatch, value ?? ""));
  }

  return evaluate(template);
}
/**
 * Resolves a path like 'foo.bar[0].baz' against an object
 */
function resolvePath(obj: any, path: string): any {
  return path
    .split(/[\.\[\]]/)
    .filter(Boolean)
    .reduce((acc, key) => (acc != null ? acc[key] : undefined), obj);
}


// export function replaceTemplate(data: any, template: string): string {
//   function evaluate(str: string): string {
//     // Match innermost expression first
//     const regex = /\$([a-zA-Z_$][\w$]*)\{([^{}]+)\}|\{([^{}]+)\}/;

//     const match = regex.exec(str);
//     if (!match) return str;

//     const [fullMatch, helperName, helperExpr, simpleExpr] = match;
//     let value: any;

//     if (helperName && helperExpr !== undefined) {
//       // Evaluate inner expression fully
//       const resolvedInner = resolvePath(data, helperExpr.trim());
//       if (!(helperName in helpers))
//         throw new Error(`Unknown helper: ${helperName}`);
//       value = helpers[helperName](resolvedInner);
//     } else if (simpleExpr !== undefined) {
//       const resolved = resolvePath(data, simpleExpr.trim());
//       value =
//         typeof resolved === "object"
//           ? JSON.stringify(resolved)
//           : resolved;
//     }

//     return evaluate(str.replace(fullMatch, value ?? ""));
//   }

//   return evaluate(template);
// }

// /**
//  * Resolves a path like 'foo.bar[0].baz' against an object
//  */
// function resolvePath(obj: any, path: string): any {
//   return path
//     .split(/[\.\[\]]/)
//     .filter(Boolean)
//     .reduce((acc, key) => (acc != null ? acc[key] : undefined), obj);
// }


// /**
//  * Replaces placeholders in a template string with values from data.
//  * Supports helpers (single or chained) using the syntax $helper{expression}.
//  */
// export function replaceTemplate(
//   data: any,
//   template: string,
// ): string {
//   function evaluate(str: string): string {
//     // Regex: matches either $helper{expr} or {expr}, innermost first
//     const regex = /\$([a-zA-Z_$][\w$]*)\{([^{}]+)\}|\{([^{}]+)\}/;

//     let match = regex.exec(str);
//     if (!match) return str;

//     const [fullMatch, helperName, helperExpr, simpleExpr] = match;

//     let value: any;

//     if (helperName && helperExpr !== undefined) {
//       // Recursively evaluate inner expression
//       const inner = evaluate(helperExpr.trim());
//       // If inner matches a path in data, resolve it
//       const resolvedInner =
//         resolvePath(data, helperExpr.trim()) ?? inner;
//       if (!helpers[helperName])
//         throw new Error(`Unknown helper: ${helperName}`);
//       value = helpers[helperName](resolvedInner);
//     } else if (simpleExpr !== undefined) {
//       value = resolvePath(data, simpleExpr.trim());
//     }

//     // Replace first match and continue recursively
//     return evaluate(str.replace(fullMatch, value ?? ""));
//   }

//   return evaluate(template);
// }


// /**
//  * Resolves a path like 'foo.bar[0].baz' against an object
//  */
// function resolvePath(obj: any, path: string): any {
//   return path
//     .split(/[\.\[\]]/)
//     .filter(Boolean)
//     .reduce((acc, key) => (acc != null ? acc[key] : undefined), obj);
// }




// ): string {
//   // Matches either {expression} or $helper{expression}
//   const regex = /(\$[a-zA-Z_$][\w$]*)?\{([^{}]+)\}/g;

//   return template.replace(regex, (_, helperName, expr) => {
//     try {
//       let value = resolvePath(data, expr.trim());
//       if (helperName) {
//         const name = helperName.slice(1); // remove $
//         if (!helpers[name]) throw new Error(`Unknown helper: ${name}`);
//         value = helpers[name](value);
//       }
//       return value ?? "";
//     } catch (e: any) {
//       return `{Error:${e.message}}`;
//     }
//   });
// }
