
export function replaceTemplate(data: any, template: string): string {
    return template.replace(/\{([^{}]+)\}/g, (_, path) => {
        return path.split('.').reduce((obj: any, key: any) => obj[key], data)
    })
}

export function handleTemplate(data: any, field: string | {template: string}, identifier = ""): string {
    if(typeof field === "string") return field;

    const template = field.template;
    if(!template || typeof template !== "string") throw new Error(`Invalid description template of ${identifier}`)

    return replaceTemplate(data, template);
}