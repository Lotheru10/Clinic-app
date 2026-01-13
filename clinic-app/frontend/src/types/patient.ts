export interface PatientDTO {
    id: number;
    firstName: string;
    lastName: string;
}

export interface PatientDetailsDTO extends PatientDTO{
    address: string;
}

export type PatientCreateRequest = {
    firstName: string;
    lastName: string;
    peselNumber: string;
    address: string;
};
