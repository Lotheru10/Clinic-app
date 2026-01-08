import { apiFetch} from "./http.ts";
import type {DoctorsOfficeDTO, DoctorsOfficeDetailsDTO, DoctorsOfficeCreateRequest} from "../types/doctorsoffice.ts";

export function getDoctorsOffices(){
    return apiFetch<DoctorsOfficeDTO[]>('/api/doctorsOffices');
}

export function getDoctorsOffice(id: number){
    return apiFetch<DoctorsOfficeDetailsDTO>(`/api/doctorsOffices/${id}`);
}

export function addDoctorsOffice(payload: DoctorsOfficeCreateRequest):Promise<DoctorsOfficeDTO> {
    return apiFetch<DoctorsOfficeDTO>("/api/doctorsOffices", {
        method: "POST",
        body: JSON.stringify(payload),
    });
}

export function deleteDoctorsOffice(id: number) {
    return apiFetch<void>(`/api/doctorsOffices/${id}`, {
            method: "DELETE",
        }
    );
}