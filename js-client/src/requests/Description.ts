import { GetRequestCauseInfo } from "../resources/ResourceHandler"
import { RequestCause } from "../types/request"
import { print } from "../ui/Console"
import { handleTemplate } from "../ui/Template"

export function getCauseDescription(cause: RequestCause) {
    const info = GetRequestCauseInfo(cause.type)
    let causeDetails =  info?.fallback ?? cause.type

    if(!info)
    print(cause)

    if(info?.description) {
        causeDetails = handleTemplate(cause.data, info.description)
    }

    return causeDetails
}