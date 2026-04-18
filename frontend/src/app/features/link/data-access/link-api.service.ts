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
      `${this.apiUrl}/save`,
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
      `${this.apiUrl}/findById/${id}`
    );
  }

  /**
   * Monta URL pública para redirecionamento.
   */
  obterUrlRedirecionamento(
    identificador: string
  ): string {

    return `${this.apiUrl}/redireciona/${identificador}`;
  }
}