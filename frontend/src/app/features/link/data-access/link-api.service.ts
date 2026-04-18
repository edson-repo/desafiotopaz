import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { LinkRequestModel } from '../models/link-request.model';
import { LinkResponseModel } from '../models/link-response.model';

/**
 * Service responsável por consumir a API do módulo de links.
 */
@Injectable({
  providedIn: 'root'
})
export class LinkApiService {
  private http = inject(HttpClient);

  private readonly apiUrl = 'http://localhost:8080/desafiotopaz/api/link';

  /**
   * Salva um novo link.
   */
  salvar(payload: LinkRequestModel): Observable<LinkResponseModel> {
    return this.http.post<LinkResponseModel>(`${this.apiUrl}/salvar`, payload);
  }

  /**
   * Busca um link pelo id.
   */
  buscarPorId(id: number): Observable<LinkResponseModel> {
    return this.http.get<LinkResponseModel>(`${this.apiUrl}/buscarPorId/${id}`);
  }

  /**
   * Busca todos os links cadastrados.
   */
  buscarTodos(): Observable<LinkResponseModel[]> {
    return this.http.get<LinkResponseModel[]>(`${this.apiUrl}/buscarTodos`);
  }

  /**
   * Atualiza um link existente.
   */
  atualizar(id: number, payload: LinkRequestModel): Observable<LinkResponseModel> {
    return this.http.put<LinkResponseModel>(`${this.apiUrl}/atualizar/${id}`, payload);
  }

  /**
   * Exclui um link pelo id.
   */
  excluir(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/excluir/${id}`);
  }

  /**
   * Monta a URL de redirecionamento para alias ou código curto.
   */
  obterUrlRedirecionamento(identificador: string): string {
    return `${this.apiUrl}/redirecionar/${identificador}`;
  }
}