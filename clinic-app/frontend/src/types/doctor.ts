export interface DoctorDTO {
    id: number;
    firstName: string;
    lastName: string;
    specialization: string;
}

export interface DoctorDetailsDTO extends DoctorDTO{
    address: string;
}

export type DoctorCreateRequest = {
    firstName: string;
    lastName: string;
    peselNumber: string;
    specialization: string;
    address: string;
};