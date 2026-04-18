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
  templateUrl: './home-page.component.html',
  styleUrls: ['./home-page.component.css']
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
    this.copiarLink(this.obterUrlResultadoCriacao());
  }

  abrirLinkResultado(): void {
    this.abrirLink(this.obterUrlResultadoCriacao());
  }

  copiarItemSelecionado(): void {
    this.copiarLink(this.obterUrlItemSelecionado());
  }

  abrirItemSelecionado(): void {
    this.abrirLink(this.obterUrlItemSelecionado());
  }

  obterUrlResultadoCriacao(): string {
    return this.montarUrlRedirecionamento(this.resultadoCriacao);
  }

  obterUrlItemSelecionado(): string {
    return this.montarUrlRedirecionamento(this.itemSelecionado);
  }

  private montarUrlRedirecionamento(item?: LinkResponseModel): string {
    if (!item) {
      return '';
    }

    const identificador = item.alias || item.codigoCurto || '';
    return this.linkApiService.obterUrlRedirecionamento(identificador);
  }

  private copiarLink(url: string): void {
    if (!url) {
      return;
    }

    navigator.clipboard.writeText(url);
  }

  private abrirLink(url: string): void {
    if (!url) {
      return;
    }

    window.open(url, '_blank');
  }

  private limparFormulario(): void {
    this.urlOriginal = '';
    this.alias = '';
  }
}