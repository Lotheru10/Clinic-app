import { apiFetch} from "./http.ts";
import type {PatientCreateRequest, PatientDetailsDTO, PatientDTO} from "../types/patient.ts";

export function getPatients(){
    return apiFetch<PatientDTO[]>('/api/patients');
}

export function getPatient(id: number){
    return apiFetch<PatientDetailsDTO>(`/api/patients/${id}`);
}

export function addPatient(payload: PatientCreateRequest):Promise<PatientDTO> {
    return apiFetch<PatientDTO>("/api/patients", {
        method: "POST",
        body: JSON.stringify(payload),
    });
}

export function deletePatient(id: number) {
    return apiFetch<void>(`/api/patients/${id}`, {
            method: "DELETE",
        }
    );
}

export function addExamplePatients() {
    return apiFetch("/api/dev/patients/add-patients", {
        method: "POST",
    });
}