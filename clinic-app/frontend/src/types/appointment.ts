export interface AppointmentDTO {
  id: number;
  doctorName: string;
  officeName: string;
  start: string;
  end: string;
}

export interface AppointmentCreateRequest {
  patientId: number;
  shiftId: number;
  start: string;
}

export interface AvailableAppointmentSlotDTO {
  shiftId: number;
  doctorId: number;
  officeId: number;
  start: string;
  end: string;
}
