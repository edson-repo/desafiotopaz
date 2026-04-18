import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { LinkRequestModel } from '../models/link-request.model';
import { LinkResponseModel } from '../models/link-response.model';

/**
 * Service responsável pela comunicação com backend.
 */
@Injectable({
  providedIn: 'root'
})
export class LinkApiService {

  private http = inject(HttpClient);

  private readonly apiUrl =
    'http://localhost:8080/desafiotopaz/api/link';

  /**
   * Cria novo link encurtado.
   */
  salvar(
    request: LinkRequestModel
  ): Observable<LinkResponseModel> {

    return this.http.post<LinkResponseModel>(
      `${this.apiUrl}/salvar`,
      request
    );
  }

  /**
   * Busca link por id.
   */
  buscarPorId(
    id: number
  ): Observable<LinkResponseModel> {

    return this.http.get<LinkResponseModel>(
      `${this.apiUrl}/buscarPorId/${id}`
    );
  }

  /**
   * Lista todos os links cadastrados.
   */
  buscarTodos(): Observable<LinkResponseModel[]> {
    return this.http.get<LinkResponseModel[]>(
      `${this.apiUrl}/buscarTodos`
    );
  }

  /**
   * Atualiza um link existente.
   */
  atualizar(
    id: number,
    request: LinkRequestModel
  ): Observable<LinkResponseModel> {
    return this.http.put<LinkResponseModel>(
      `${this.apiUrl}/atualizar/${id}`,
      request
    );
  }

  /**
   * Exclui um link pelo id.
   */
  excluir(
    id: number
  ): Observable<void> {
    return this.http.delete<void>(
      `${this.apiUrl}/excluir/${id}`
    );
  }

  /**
   * Monta URL pública para redirecionamento.
   */
  obterUrlRedirecionamento(
    identificador: string
  ): string {

    return `${this.apiUrl}/redirecionar/${identificador}`;
  }
}