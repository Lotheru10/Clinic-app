import type {DoctorDTO} from "./doctor.ts";
import type {DoctorsOfficeDTO} from "./doctorsoffice.ts";

export interface DoctorOfficeShiftDTO {
    doctorId: number;
    doctorName: string;
    start: string;
    end: string;
}

export interface DoctorShiftDTO {
    officeId: number;
    officeName: string;
    start: string;
    end: string;
}
export interface ShiftDTO {
    id: number;
    doctorId: number;
    doctorName: string;
    officeId: number;
    officeName: string;
    start: string;
    end: string;
}

export interface CreateShiftDTO {
    doctorId: number;
    officeId: number;
    start: string;
    end: string;
}

export interface AvailabilityDTO {
    freeDoctors: DoctorDTO[];
    freeDoctorsOffices: DoctorsOfficeDTO[];
}
