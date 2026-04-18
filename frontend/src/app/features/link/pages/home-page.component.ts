import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { LinkApiService } from '../data-access/link-api.service';
import { LinkResponseModel } from '../models/link-response.model';

/**
 * Página principal do encurtador.
 */
@Component({
  selector: 'app-home-page',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="pagina">

      <div class="card">

        <h1>Desafio Topaz</h1>
        <p class="subtitulo">
          Encurtador de URL
        </p>

        <label>URL Original</label>
        <input
          type="text"
          [(ngModel)]="urlOriginal"
          placeholder="https://www.google.com"
        />

        <label>Alias (opcional)</label>
        <input
          type="text"
          [(ngModel)]="alias"
          placeholder="meulink"
        />

        <button
          (click)="encurtar()"
          [disabled]="carregando"
        >
          {{ carregando ? 'Processando...' : 'Encurtar URL' }}
        </button>

        <div
          *ngIf="mensagemErro"
          class="erro"
        >
          {{ mensagemErro }}
        </div>

        <div
          *ngIf="resultado"
          class="resultado"
        >
          <p><strong>Link gerado:</strong></p>

          <a
            [href]="resultado.urlEncurtada"
            target="_blank"
          >
            {{ resultado.urlEncurtada }}
          </a>

          <div class="acoes">
            <button (click)="copiarLink()">
              Copiar
            </button>

            <button (click)="abrirLink()">
              Abrir
            </button>
          </div>
        </div>

      </div>

    </div>
  `,
  styles: [`
    .pagina{
      min-height:100vh;
      background:#f4f6f8;
      display:flex;
      justify-content:center;
      align-items:center;
      padding:20px;
    }

    .card{
      width:100%;
      max-width:520px;
      background:#ffffff;
      padding:32px;
      border-radius:14px;
      box-shadow:0 10px 30px rgba(0,0,0,.08);
    }

    h1{
      margin:0;
      font-size:28px;
      color:#111827;
    }

    .subtitulo{
      margin:8px 0 24px;
      color:#6b7280;
    }

    label{
      display:block;
      margin-bottom:6px;
      margin-top:14px;
      font-weight:600;
    }

    input{
      width:100%;
      padding:12px;
      border:1px solid #d1d5db;
      border-radius:8px;
      font-size:14px;
      box-sizing:border-box;
    }

    button{
      margin-top:18px;
      width:100%;
      border:none;
      padding:12px;
      border-radius:8px;
      background:#2563eb;
      color:#fff;
      font-weight:600;
      cursor:pointer;
    }

    button:disabled{
      opacity:.7;
      cursor:not-allowed;
    }

    .erro{
      margin-top:16px;
      padding:12px;
      border-radius:8px;
      background:#fee2e2;
      color:#991b1b;
    }

    .resultado{
      margin-top:20px;
      padding:16px;
      border-radius:8px;
      background:#ecfeff;
    }

    .resultado a{
      word-break:break-all;
      color:#2563eb;
      text-decoration:none;
    }

    .acoes{
      display:flex;
      gap:10px;
      margin-top:14px;
    }

    .acoes button{
      margin-top:0;
    }
  `]
})
export class HomePageComponent {

  private linkApiService = inject(LinkApiService);

  urlOriginal = '';
  alias = '';

  carregando = false;
  mensagemErro = '';

  resultado?: LinkResponseModel;

  /**
   * Chama backend para encurtar URL.
   */
  encurtar(): void {

    this.mensagemErro = '';
    this.resultado = undefined;

    this.carregando = true;

    this.linkApiService.salvar({
      urlOriginal: this.urlOriginal,
      alias: this.alias || undefined
    })
    .subscribe({
      next: resposta => {
        this.resultado = resposta;
        this.carregando = false;
      },
      error: erro => {
        this.mensagemErro =
          erro?.error?.mensagem ||
          'Erro ao processar solicitação.';

        this.carregando = false;
      }
    });
  }

  /**
   * Copia link para área de transferência.
   */
  copiarLink(): void {

    if (!this.resultado) {
      return;
    }

    navigator.clipboard.writeText(
      this.resultado.urlEncurtada
    );
  }

  /**
   * Abre link gerado.
   */
  abrirLink(): void {

    if (!this.resultado) {
      return;
    }

    window.open(
      this.resultado.urlEncurtada,
      '_blank'
    );
  }
}