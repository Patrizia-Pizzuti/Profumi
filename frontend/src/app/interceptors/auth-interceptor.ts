import { HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  intercept(req: HttpRequest<any>, next: HttpHandler) {
    // ✅ Recupera il token dal localStorage
    const authToken = localStorage.getItem("token");

    // ✅ Se il token è null, invia la richiesta senza modifiche
    if (!authToken) {
      return next.handle(req);
    }

    // ✅ Clona la richiesta e aggiunge l'header di autorizzazione con il token
    const authReq = req.clone({
      headers: req.headers.set('Authorization', `Bearer ${authToken}`)
    });

    // ✅ Invia la richiesta con l'header di autenticazione aggiornato
    return next.handle(authReq);
  }
}
