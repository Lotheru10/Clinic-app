import { apiFetch } from "./http.ts";
import type { CreateOpinionDTO } from "../types/opinion.ts"


export function addOpinion(payload: CreateOpinionDTO){
    console.log("api:", payload);
    return apiFetch<void>(
        "/api/opinions", {
            method: "POST",
            body: JSON.stringify(payload),
    });
}