import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { LinkApiService } from '../data-access/link-api.service';
import { LinkResponseModel } from '../models/link-response.model';

/**
 * Página principal do encurtador.
 * Mantém o cadastro do lado esquerdo e a lista de links do lado direito.
 */
@Component({
  selector: 'app-home-page',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="pagina">
      <div class="conteudo">

        <div class="cabecalho">
          <h1>Desafio Topaz</h1>
          <p>Encurtador de URL</p>
        </div>

        <div class="grid">

          <div class="card">
            <h2>{{ modoEdicao ? 'Editar Link' : 'Encurtar URL' }}</h2>

            <label for="urlOriginal">URL Original</label>
            <input
              id="urlOriginal"
              type="text"
              [(ngModel)]="urlOriginal"
              list="lista-url-original"
              placeholder="https://www.google.com"
            />
            <datalist id="lista-url-original">
              <option *ngFor="let item of listaUrlOriginal" [value]="item"></option>
            </datalist>

            <label for="alias">Alias (opcional)</label>
            <input
              id="alias"
              type="text"
              [(ngModel)]="alias"
              list="lista-alias"
              placeholder="meulink"
            />
            <datalist id="lista-alias">
              <option *ngFor="let item of listaAlias" [value]="item"></option>
            </datalist>

            <button
              (click)="salvarOuAtualizar()"
              [disabled]="carregandoFormulario"
            >
              {{ carregandoFormulario ? 'Processando...' : (modoEdicao ? 'Atualizar Link' : 'Encurtar URL') }}
            </button>

            <button
              *ngIf="modoEdicao"
              class="botao-secundario"
              (click)="cancelarEdicao()"
              [disabled]="carregandoFormulario"
            >
              Cancelar Edição
            </button>

            <div
              *ngIf="mensagemErroFormulario"
              class="erro"
            >
              {{ mensagemErroFormulario }}
            </div>

            <div
              *ngIf="resultadoCriacao"
              class="resultado"
            >
              <p><strong>Link gerado:</strong></p>

              <a
                [href]="obterUrlResultadoCriacao()"
                target="_blank"
              >
                {{ obterUrlResultadoCriacao() }}
              </a>

              <div class="acoes">
                <button (click)="copiarLinkResultado()">
                  Copiar
                </button>

                <button (click)="abrirLinkResultado()">
                  Abrir
                </button>
              </div>

              <div class="detalhes">
                <p><strong>ID:</strong> {{ resultadoCriacao.id }}</p>
                <p><strong>Alias:</strong> {{ resultadoCriacao.alias || '-' }}</p>
                <p><strong>Código Curto:</strong> {{ resultadoCriacao.codigoCurto || '-' }}</p>
                <p><strong>Data Criação:</strong> {{ resultadoCriacao.dataCriacao || '-' }}</p>
              </div>
            </div>
          </div>

          <div class="card">
            <div class="topo-lista">
              <h2>Links Cadastrados</h2>
              <button
                class="botao-recarregar"
                (click)="carregarLista()"
                [disabled]="carregandoLista"
              >
                {{ carregandoLista ? 'Carregando...' : 'Recarregar' }}
              </button>
            </div>

            <div
              *ngIf="mensagemErroLista"
              class="erro"
            >
              {{ mensagemErroLista }}
            </div>

            <div
              *ngIf="!carregandoLista && listaLinks.length === 0"
              class="vazio"
            >
              Nenhum link cadastrado até o momento.
            </div>

            <div class="lista-links">
              <button
                type="button"
                class="item-link"
                *ngFor="let item of listaLinks"
                [class.selecionado]="itemSelecionado?.id === item.id"
                (click)="selecionarItem(item)"
              >
                <div class="item-titulo">
                  {{ item.alias || item.codigoCurto || ('ID ' + item.id) }}
                </div>

                <div class="item-subtitulo">
                  {{ item.urlOriginal }}
                </div>
              </button>
            </div>

            <div
              *ngIf="itemSelecionado"
              class="resultado"
            >
              <p><strong>Item selecionado:</strong></p>

              <a
                [href]="obterUrlItemSelecionado()"
                target="_blank"
              >
                {{ obterUrlItemSelecionado() }}
              </a>

              <div class="detalhes">
                <p><strong>ID:</strong> {{ itemSelecionado.id }}</p>
                <p><strong>URL Original:</strong> {{ itemSelecionado.urlOriginal }}</p>
                <p><strong>Alias:</strong> {{ itemSelecionado.alias || '-' }}</p>
                <p><strong>Código Curto:</strong> {{ itemSelecionado.codigoCurto || '-' }}</p>
                <p><strong>Data Criação:</strong> {{ itemSelecionado.dataCriacao || '-' }}</p>
              </div>

              <div class="acoes-lista">
                <button (click)="editarItemSelecionado()">
                  Editar
                </button>

                <button (click)="excluirItemSelecionado()" class="botao-excluir">
                  Excluir
                </button>

                <button (click)="copiarItemSelecionado()">
                  Copiar
                </button>

                <button (click)="abrirItemSelecionado()">
                  Abrir
                </button>
              </div>
            </div>
          </div>

        </div>
      </div>
    </div>
  `,
  styles: [`
    .pagina {
      min-height: 100vh;
      background: #f4f6f8;
      padding: 32px 20px;
      box-sizing: border-box;
    }

    .conteudo {
      max-width: 1200px;
      margin: 0 auto;
    }

    .cabecalho {
      text-align: center;
      margin-bottom: 28px;
    }

    .cabecalho h1 {
      margin: 0;
      font-size: 32px;
      color: #111827;
    }

    .cabecalho p {
      margin-top: 8px;
      color: #6b7280;
      font-size: 16px;
    }

    .grid {
      display: grid;
      grid-template-columns: repeat(2, 1fr);
      gap: 20px;
    }

    .card {
      background: #ffffff;
      border-radius: 14px;
      padding: 24px;
      box-shadow: 0 10px 30px rgba(0, 0, 0, .08);
    }

    .card h2 {
      margin-top: 0;
      margin-bottom: 18px;
      font-size: 22px;
      color: #111827;
    }

    label {
      display: block;
      margin-top: 14px;
      margin-bottom: 6px;
      font-weight: 600;
      color: #111827;
    }

    input {
      width: 100%;
      padding: 12px;
      border: 1px solid #d1d5db;
      border-radius: 8px;
      font-size: 14px;
      box-sizing: border-box;
      background: #ffffff;
    }

    button {
      margin-top: 18px;
      width: 100%;
      border: none;
      padding: 12px;
      border-radius: 8px;
      background: #2563eb;
      color: #ffffff;
      font-weight: 600;
      cursor: pointer;
      transition: .2s ease;
    }

    button:hover {
      opacity: .95;
    }

    button:disabled {
      opacity: .7;
      cursor: not-allowed;
    }

    .botao-secundario {
      background: #6b7280;
    }

    .botao-excluir {
      background: #dc2626;
    }

    .botao-recarregar {
      width: auto;
      margin-top: 0;
      padding: 10px 16px;
    }

    .erro {
      margin-top: 16px;
      padding: 12px;
      border-radius: 8px;
      background: #fee2e2;
      color: #991b1b;
      font-size: 14px;
    }

    .resultado {
      margin-top: 20px;
      padding: 16px;
      border-radius: 8px;
      background: #ecfeff;
    }

    .resultado a {
      display: inline-block;
      margin-top: 6px;
      color: #2563eb;
      text-decoration: none;
      word-break: break-all;
    }

    .acoes {
      display: flex;
      gap: 10px;
      margin-top: 14px;
    }

    .acoes button {
      margin-top: 0;
    }

    .acoes-lista {
      display: grid;
      grid-template-columns: repeat(2, 1fr);
      gap: 10px;
      margin-top: 16px;
    }

    .acoes-lista button {
      margin-top: 0;
    }

    .detalhes {
      margin-top: 16px;
      font-size: 14px;
      color: #374151;
      word-break: break-word;
    }

    .detalhes p {
      margin: 6px 0;
    }

    .topo-lista {
      display: flex;
      justify-content: space-between;
      align-items: center;
      gap: 12px;
      margin-bottom: 16px;
    }

    .topo-lista h2 {
      margin: 0;
    }

    .lista-links {
      display: flex;
      flex-direction: column;
      gap: 10px;
      max-height: 320px;
      overflow-y: auto;
    }

    .item-link {
      width: 100%;
      text-align: left;
      background: #f9fafb;
      color: #111827;
      border: 1px solid #e5e7eb;
      padding: 14px;
      border-radius: 10px;
      margin-top: 0;
    }

    .item-link.selecionado {
      border-color: #2563eb;
      background: #eff6ff;
    }

    .item-titulo {
      font-weight: 700;
      margin-bottom: 6px;
    }

    .item-subtitulo {
      font-size: 13px;
      color: #4b5563;
      word-break: break-word;
    }

    .vazio {
      padding: 16px;
      border-radius: 8px;
      background: #f9fafb;
      color: #6b7280;
      font-size: 14px;
    }

    @media (max-width: 900px) {
      .grid {
        grid-template-columns: 1fr;
      }
    }

    @media (max-width: 640px) {
      .acoes {
        flex-direction: column;
      }

      .acoes-lista {
        grid-template-columns: 1fr;
      }

      .topo-lista {
        flex-direction: column;
        align-items: stretch;
      }

      .botao-recarregar {
        width: 100%;
      }
    }
  `]
})
export class HomePageComponent implements OnInit {

  private linkApiService = inject(LinkApiService);

  urlOriginal = '';
  alias = '';

  listaUrlOriginal: string[] = [
    'https://www.google.com',
    'https://www.youtube.com',
    'https://www.github.com',
    'https://www.microsoft.com',
    'https://www.angular.dev',
    'https://www.oracle.com',
    'https://www.stackoverflow.com',
    'https://www.linkedin.com',
    'https://www.openai.com',
    'https://www.uol.com.br'
  ];

  listaAlias: string[] = [
    'google',
    'youtube',
    'github',
    'microsoft',
    'angular',
    'oracle',
    'stack',
    'linkedin',
    'openai',
    'uol'
  ];

  carregandoFormulario = false;
  carregandoLista = false;

  mensagemErroFormulario = '';
  mensagemErroLista = '';

  resultadoCriacao?: LinkResponseModel;

  listaLinks: LinkResponseModel[] = [];
  itemSelecionado?: LinkResponseModel;

  modoEdicao = false;
  idEmEdicao?: number;

  ngOnInit(): void {
    this.carregarLista();
  }

  salvarOuAtualizar(): void {
    if (this.modoEdicao && this.idEmEdicao) {
      this.atualizar();
      return;
    }

    this.salvar();
  }

  salvar(): void {
    this.mensagemErroFormulario = '';
    this.resultadoCriacao = undefined;

    if (!this.urlOriginal || !this.urlOriginal.trim()) {
      this.mensagemErroFormulario = 'Informe a URL original.';
      return;
    }

    this.carregandoFormulario = true;

    this.linkApiService.salvar({
      urlOriginal: this.urlOriginal,
      alias: this.alias || undefined
    }).subscribe({
      next: resposta => {
        this.resultadoCriacao = resposta;
        this.carregandoFormulario = false;
        this.limparFormulario();
        this.carregarLista();
      },
      error: erro => {
        this.mensagemErroFormulario =
          erro?.error?.mensagem || 'Erro ao processar solicitação.';
        this.carregandoFormulario = false;
      }
    });
  }

  atualizar(): void {
    if (!this.idEmEdicao) {
      return;
    }

    this.mensagemErroFormulario = '';
    this.resultadoCriacao = undefined;

    if (!this.urlOriginal || !this.urlOriginal.trim()) {
      this.mensagemErroFormulario = 'Informe a URL original.';
      return;
    }

    this.carregandoFormulario = true;

    this.linkApiService.atualizar(this.idEmEdicao, {
      urlOriginal: this.urlOriginal,
      alias: this.alias || undefined
    }).subscribe({
      next: resposta => {
        this.resultadoCriacao = resposta;
        this.carregandoFormulario = false;
        this.modoEdicao = false;
        this.idEmEdicao = undefined;
        this.limparFormulario();
        this.carregarLista();
      },
      error: erro => {
        this.mensagemErroFormulario =
          erro?.error?.mensagem || 'Erro ao atualizar link.';
        this.carregandoFormulario = false;
      }
    });
  }

  carregarLista(): void {
    this.carregandoLista = true;
    this.mensagemErroLista = '';

    this.linkApiService.buscarTodos().subscribe({
      next: resposta => {
        this.listaLinks = resposta;
        this.carregandoLista = false;

        if (this.itemSelecionado) {
          this.itemSelecionado = this.listaLinks.find(
            item => item.id === this.itemSelecionado?.id
          );
        }
      },
      error: erro => {
        this.mensagemErroLista =
          erro?.error?.mensagem || 'Erro ao carregar lista.';
        this.carregandoLista = false;
      }
    });
  }

  selecionarItem(item: LinkResponseModel): void {
    this.itemSelecionado = item;
  }

  editarItemSelecionado(): void {
    if (!this.itemSelecionado) {
      return;
    }

    this.modoEdicao = true;
    this.idEmEdicao = this.itemSelecionado.id;
    this.urlOriginal = this.itemSelecionado.urlOriginal;
    this.alias = this.itemSelecionado.alias || '';
    this.resultadoCriacao = undefined;
    this.mensagemErroFormulario = '';
  }

  excluirItemSelecionado(): void {
    if (!this.itemSelecionado) {
      return;
    }

    const confirmou = window.confirm('Deseja realmente excluir este link?');

    if (!confirmou) {
      return;
    }

    this.linkApiService.excluir(this.itemSelecionado.id).subscribe({
      next: () => {
        this.itemSelecionado = undefined;
        this.carregarLista();
      },
      error: erro => {
        this.mensagemErroLista =
          erro?.error?.mensagem || 'Erro ao excluir link.';
      }
    });
  }

  cancelarEdicao(): void {
    this.modoEdicao = false;
    this.idEmEdicao = undefined;
    this.mensagemErroFormulario = '';
    this.limparFormulario();
  }

  copiarLinkResultado(): void {
    const url = this.obterUrlResultadoCriacao();

    if (!url) {
      return;
    }

    navigator.clipboard.writeText(url);
  }

  abrirLinkResultado(): void {
    const url = this.obterUrlResultadoCriacao();

    if (!url) {
      return;
    }

    window.open(url, '_blank');
  }

  copiarItemSelecionado(): void {
    const url = this.obterUrlItemSelecionado();

    if (!url) {
      return;
    }

    navigator.clipboard.writeText(url);
  }

  abrirItemSelecionado(): void {
    const url = this.obterUrlItemSelecionado();

    if (!url) {
      return;
    }

    window.open(url, '_blank');
  }

  obterUrlResultadoCriacao(): string {
    if (!this.resultadoCriacao) {
      return '';
    }

    const identificador =
      this.resultadoCriacao.alias || this.resultadoCriacao.codigoCurto || '';

    return this.linkApiService.obterUrlRedirecionamento(identificador);
  }

  obterUrlItemSelecionado(): string {
    if (!this.itemSelecionado) {
      return '';
    }

    const identificador =
      this.itemSelecionado.alias || this.itemSelecionado.codigoCurto || '';

    return this.linkApiService.obterUrlRedirecionamento(identificador);
  }

  limparFormulario(): void {
    this.urlOriginal = '';
    this.alias = '';
  }
}