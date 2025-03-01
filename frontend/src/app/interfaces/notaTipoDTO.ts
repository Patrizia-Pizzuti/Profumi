export interface NotaTipoDTO {
  id: number;           // ID della nota
  nomeNota: string;     // Nome della nota (es. "Bergamotto", "Rosa")
  tipoNotaId: number | null;  // ID del tipo di nota (1 = Testa, 2 = Cuore, 3 = Fondo)
  tipoNotaNome: string; // Nome del tipo di nota (es. "Testa", "Cuore", "Fondo")
}
