import type {DoctorOfficeShiftDTO} from "./shift.ts";

export interface DoctorsOfficeDTO {
    id: number;
    roomNumber: string;
}

export interface DoctorsOfficeDetailsDTO extends DoctorsOfficeDTO{
    roomDescription: string;
    shifts: DoctorOfficeShiftDTO[];
}

export type DoctorsOfficeCreateRequest = {
    roomNumber: string;
    roomDescription: string;
};