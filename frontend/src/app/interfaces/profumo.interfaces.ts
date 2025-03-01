import { NotaTipoDTO } from "./notaTipoDTO";

export interface Profumo {
    id?: number;
    nome: string;
    marca: string;
    descrizione: string;
    formato: string;
    prezzo: number;
    immagineUrl?: string; // URL dell'immagine (opzionale)
    note: NotaTipoDTO[];  // ✅ Ora contiene oggetti completi, non solo ID
}
