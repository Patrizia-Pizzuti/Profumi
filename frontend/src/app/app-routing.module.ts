import { ProfumiDettaglioComponent } from './profumo-dettaglio/profumo-dettaglio.component';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';

import { NoteComponent } from './note/note.component';
import { LoginComponent } from './auth/login/login.component';
import { RegisterComponent } from './auth/register/register.component';
import { NoteCrudComponent } from './note-crud/note-crud.component';
import { AuthGuard } from './guards/auth.guards';
import { CrudProfumiComponent } from './crud-profumi/crud-profumi.component';
import { ProfumiComponent } from './profumi/profumi.component';
import { ManageRolesComponent } from './components/manage-roles/manage-roles.component';

const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'profumi', component: ProfumiComponent },
  //{ path: 'profumi/:id', component: ProfumiComponent }, // ✅ Nuova rotta per i profumi di una nota
  { path: 'profumo/:id', component: ProfumiDettaglioComponent },
  { path: 'note', component: NoteComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'crud-profumi', component: CrudProfumiComponent, canActivate: [AuthGuard] },
  { path: 'crud-note', component: NoteCrudComponent, canActivate: [AuthGuard] },
   // 👇 Percorso per la gestione utenti (visibile solo agli Admin)
   { path: 'gestione-utenti', component: ManageRolesComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
