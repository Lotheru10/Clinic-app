import type { AppointmentDTO } from "./appointment";

export interface PatientDTO {
  id: number;
  firstName: string;
  lastName: string;
}

export interface PatientDetailsDTO extends PatientDTO {
  address: string;
  appointments: AppointmentDTO[];
}

export type PatientCreateRequest = {
  firstName: string;
  lastName: string;
  peselNumber: string;
  address: string;
};
