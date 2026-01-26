export interface OpinionDTO {
    rate: number;
    message: string;
    patientName: string;
    date: string;
}

export interface CreateOpinionDTO extends OpinionDTO {
    rate: number;
    message: string;
    appointmentId: number;
}

