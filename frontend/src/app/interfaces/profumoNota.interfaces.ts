import { Nota } from "./note.interfaces";
import { Profumo } from "./profumo.interfaces";
import { TipoNota } from "./tipoNota.interfaces";

export interface ProfumoNota {
    id: number;
    profumo: Profumo;
    nota: Nota;
    tipoNota: TipoNota;
  }
  