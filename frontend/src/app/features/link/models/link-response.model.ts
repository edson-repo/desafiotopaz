/**
 * Dados devolvidos pela API após criação.
 */
export interface LinkResponseModel {
  id: number;
  urlOriginal: string;
  alias: string | null;
  codigoCurto: string | null;
  urlEncurtada: string;
  dataCriacao: string;
}