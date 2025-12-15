import { apiFetch} from "./http.ts";
import type {DoctorDTO, DoctorDetailsDTO, DoctorCreateRequest} from "../types/doctor.ts";

export function getDoctors(){
    return apiFetch<DoctorDTO[]>('/api/doctors');
}

export function getDoctor(id: number){
    return apiFetch<DoctorDetailsDTO>(`/api/doctors/${id}`);
}

export function addDoctor(payload: DoctorCreateRequest):Promise<DoctorDTO> {
    return apiFetch<DoctorDTO>("/api/doctors", {
        method: "POST",
        body: JSON.stringify(payload),
    });
}

export function deleteDoctor(id: number) {
    return apiFetch<void>(`/api/doctors/${id}`, {
        method: "DELETE",
        }
    );
}

export function addExampleDoctors() {
    return apiFetch("/api/dev/doctors/add-doctors", {
        method: "POST",
    });
}