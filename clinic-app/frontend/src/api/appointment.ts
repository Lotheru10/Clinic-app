import { apiFetch } from "./http.ts";
import type {
  AppointmentDTO,
  AppointmentCreateRequest,
  AvailableAppointmentSlotDTO,
} from "../types/appointment.ts";

const ASSIGNMENT_URL = "/api/appointment-assigment";
const APPOINTMENT_URL = "/api/appointments";

export function getAvailableSlots(
  doctorId: number,
  start: string,
  end: string,
) {
  const params = new URLSearchParams({
    doctorId: doctorId.toString(),
    start: start,
    end: end,
  });

  return apiFetch<AvailableAppointmentSlotDTO[]>(
    `${ASSIGNMENT_URL}/availability?${params.toString()}`,
  );
}

export function addAppointment(
  payload: AppointmentCreateRequest,
): Promise<AppointmentDTO> {
  return apiFetch<AppointmentDTO>(APPOINTMENT_URL, {
    method: "POST",
    body: JSON.stringify(payload),
  });
}
