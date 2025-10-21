import { GetRequestCauseInfo } from "../resources/ResourceHandler"
import { RequestCause } from "../types/request"
import { Color } from "../ui/Color"
import { print } from "../ui/Console"
import { handleTemplate } from "../ui/Template"

export function getCauseDescription(cause: RequestCause) {
    const info = GetRequestCauseInfo(cause.type)
    let causeDetails =  info?.fallback ?? cause.type

    if(!info)
    print(cause)

    if(info?.description) {
        try {
            causeDetails = handleTemplate(cause.data, info.description)
        } catch(error: unknown) {
            print(Color.red((error as Error).message))
        }
    }

    return causeDetails
}