import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';

// 📌 COMPONENTI
import { HomeComponent } from './home/home.component';
import { ProfumiComponent } from './profumi/profumi.component';
import { NoteComponent } from './note/note.component';
import { LoginComponent } from './auth/login/login.component';
import { RegisterComponent } from './auth/register/register.component';
import { NoteCrudComponent } from './note-crud/note-crud.component';
import { NavbarComponent } from './navbar/navbar.component';
import { NotaProfumiComponent } from './dialogs/nota-profumi/nota-profumi.component';
import { ProfumiDettaglioComponent } from './profumo-dettaglio/profumo-dettaglio.component';
import { CrudProfumiComponent } from './crud-profumi/crud-profumi.component';


// 📌 MODULI ANGULAR
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { RouterModule } from '@angular/router';

// 📌 INTERCEPTORS
import { AuthInterceptor } from './interceptors/auth-interceptor';

// 📌 ANGULAR MATERIAL
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatDialogModule } from '@angular/material/dialog';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatListModule } from '@angular/material/list';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatOptionModule } from '@angular/material/core';
import { MatTableModule } from '@angular/material/table';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { ManageRolesComponent } from './components/manage-roles/manage-roles.component';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    ProfumiComponent,
    NoteComponent,
    ProfumiDettaglioComponent,
    NotaProfumiComponent,
    LoginComponent,
    RegisterComponent,
    NoteCrudComponent,
    NavbarComponent,
    CrudProfumiComponent,
    ManageRolesComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    BrowserAnimationsModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule,
    MatSnackBarModule,

    // 📌 Angular Material Modules
    MatButtonModule,
    MatIconModule,
    MatCardModule,
    MatDialogModule,
    MatToolbarModule,
    MatSidenavModule,
    MatListModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatOptionModule,
    MatTableModule, // ✅ Aggiunto per supportare tabelle
    MatSnackBarModule // ✅ Aggiunto per notifiche Snackbar
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
