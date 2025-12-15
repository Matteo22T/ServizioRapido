import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AutenticazioneService } from '../../service/autenticazione-service';
import { ChangeDetectorRef } from '@angular/core';


@Component({
  selector: 'app-reset-password',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './reset-password.html',
  styleUrls: ['./reset-password.css']
})
export class ResetPassword implements OnInit {
  token = '';
  nuovaPassword = '';
  messaggio = '';

  mostraToast = false;
  isErrore = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private authService: AutenticazioneService,
    private cd: ChangeDetectorRef
  ) {}

  ngOnInit() {
    // Legge il parametro ?token=... dall'URL
    this.token = this.route.snapshot.queryParams['token'];
  }

  salvaPassword() {
    if(!this.token) {
      this.messaggio = "Token mancante. Riprova dal link email.";
      this.isErrore = true; // Diventa rosso
      this.mostraToast = true;

      this.cd.detectChanges();

      setTimeout(() => this.mostraToast = false, 3000);
      return;
    }

    this.authService.eseguiReset(this.token, this.nuovaPassword).subscribe({
      next: (res: any) => {
        this.messaggio = res;

        this.isErrore = false;
        this.mostraToast = true;
        this.cd.detectChanges();

        setTimeout(() => {
          this.mostraToast = false;
          this.router.navigate(['/login']);
        }, 3000);
      },
      error: (err) => {
        this.messaggio = err?.error?.message ?? 'Errore di connessione. Riprova più tardi.';
        this.isErrore = true;
        this.mostraToast = true;

        this.cd.detectChanges();

        setTimeout(() => this.mostraToast = false, 3000);      }
    });
  }
}
