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
    specialization: DoctorSpecialization | "";
    address: string;
};

// @ts-ignore
export enum DoctorSpecialization {
    alergologia = "alergologia",
    anestezjologia_i_intensywna_terapia = "anestezjologia i intensywna terapia",
    angiologia = "angiologia",
    audiologia_i_foniatria = "audiologia i foniatria",
    balneologia_i_medycyna_fizykalna = "balneologia i medycyna fizykalna",
    chirurgia_dziecieca = "chirurgia dziecięca",
    chirurgia_klatki_piersiowej = "chirurgia klatki piersiowej",
    chirurgia_naczyniowa = "chirurgia naczyniowa",
    chirurgia_ogolna = "chirurgia ogólna",
    chirurgia_onkologiczna = "chirurgia onkologiczna",
    chirurgia_plastyczna = "chirurgia plastyczna",
    chirurgia_szczekowo_twarzowa = "chirurgia szczękowo-twarzowa",
    choroby_pluc = "choroby płuc",
    choroby_pluc_dzieci = "choroby płuc dzieci",
    choroby_wewnetrzne = "choroby wewnętrzne",
    choroby_zakazne = "choroby zakaźne",
    dermatologia_i_wenerologia = "dermatologia i wenerologia",
    diabetologia = "diabetologia",
    diagnostyka_laboratoryjna = "diagnostyka laboratoryjna",
    endokrynologia = "endokrynologia",
    endokrynologia_ginekologiczna_i_rozrodczosc = "endokrynologia ginekologiczna i rozrodczość",
    endokrynologia_i_diabetologia_dziecieca = "endokrynologia i diabetologia dziecięca",
    epidemiologia = "epidemiologia",
    farmakologia_kliniczna = "farmakologia kliniczna",
    gastroenterologia = "gastroenterologia",
    gastroenterologia_dziecieca = "gastroenterologia dziecięca",
    genetyka_kliniczna = "genetyka kliniczna",
    geriatria = "geriatria",
    ginekologia_onkologiczna = "ginekologia onkologiczna",
    hematologia = "hematologia",
    hipertensjologia = "hipertensjologia",
    immunologia_kliniczna = "immunologia kliniczna",
    intensywna_terapia = "intensywna terapia",
    kardiochirurgia = "kardiochirurgia",
    kardiologia = "kardiologia",
    kardiologia_dziecieca = "kardiologia dziecięca",
    medycyna_lotnicza = "medycyna lotnicza",
    medycyna_morska_i_tropikalna = "medycyna morska i tropikalna",
    medycyna_nuklearna = "medycyna nuklearna",
    medycyna_paliatywna = "medycyna paliatywna",
    medycyna_pracy = "medycyna pracy",
    medycyna_ratunkowa = "medycyna ratunkowa",
    medycyna_rodzinna = "medycyna rodzinna",
    medycyna_sadowa = "medycyna sądowa",
    medycyna_sportowa = "medycyna sportowa",
    mikrobiologia_lekarska = "mikrobiologia lekarska",
    nefrologia = "nefrologia",
    nefrologia_dziecieca = "nefrologia dziecięca",
    neonatologia = "neonatologia",
    neurochirurgia = "neurochirurgia",
    neurologia = "neurologia",
    neurologia_dziecieca = "neurologia dziecięca",
    neuropatologia = "neuropatologia",
    okulistyka = "okulistyka",
    onkologia_i_hematologia_dziecieca = "onkologia i hematologia dziecięca",
    onkologia_kliniczna = "onkologia kliniczna",
    ortopedia_i_traumatologia_narzadu_ruchu = "ortopedia i traumatologia narządu ruchu",
    otorynolaryngologia = "otorynolaryngologia",
    otorynolaryngologia_dziecieca = "otorynolaryngologia dziecięca",
    patomorfologia = "patomorfologia",
    pediatria = "pediatria",
    pediatria_metaboliczna = "pediatria metaboliczna",
    perinatologia = "perinatologia",
    poloznictwo_i_ginekologia = "położnictwo i ginekologia",
    psychiatria = "psychiatria",
    psychiatria_dzieci_i_mlodziezy = "psychiatria dzieci i młodzieży",
    radiologia_i_diagnostyka_obrazowa = "radiologia i diagnostyka obrazowa",
    radioterapia_onkologiczna = "radioterapia onkologiczna",
    rehabilitacja_medyczna = "rehabilitacja medyczna",
    reumatologia = "reumatologia",
    seksuologia = "seksuologia",
    toksykologia_kliniczna = "toksykologia kliniczna",
    transfuzjologia_kliniczna = "transfuzjologia kliniczna",
    transplantologia_kliniczna = "transplantologia kliniczna",
    urologia = "urologia",
    urologia_dziecieca = "urologia dziecięca",
    zdrowie_publiczne = "zdrowie publiczne",
};