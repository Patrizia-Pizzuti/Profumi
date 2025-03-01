export interface User {
  id?: number; // ✅ ID opzionale perché sarà assegnato dal backend
  username: string;
  email: string;
  password?: string; // ✅ Opzionale per evitare errori quando non necessario
  roles?: string[]; // ✅ Opzionale, verrà assegnato dal backend
  token?: string; // Il token sarà restituito dal backend dopo il login
}
