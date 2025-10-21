import { GetPointInfo } from "../resources/ResourceHandler";
import { Point } from "../types/point/point";
import { handleTemplate } from "./Template";

export function buildPoints(points: Point[]) {
    const sb: string[] = []

    points.sort((a, b) => a.pointType.localeCompare(b.pointType)).forEach((point, index) => {
        const name = handleTemplate({}, GetPointInfo(point.pointType).name)
        
        sb.push(`${name}: ${point.amount}`)
    })

    return sb.join(" | ")
}