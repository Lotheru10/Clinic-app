import { apiFetch } from "./http.ts";
import type {AvailabilityDTO, CreateShiftDTO, ShiftDTO} from "../types/shift.ts";

//const SHIFT_URL = '/api/shifts';
//const ASSIGNMENT_URL = '/api/shift-assignment';

export function checkAvailability(start: string, end: string) {
    const params = new URLSearchParams({
        start: start,
        end: end
    });

    return apiFetch<AvailabilityDTO>(`/api/shift-assignment/availability?${params.toString()}`);
}

export function createShift(payload: CreateShiftDTO) {
    return apiFetch<ShiftDTO>('/api/shifts', {
        method: "POST",
        body: JSON.stringify(payload),
    });
}