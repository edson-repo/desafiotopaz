/**
 * Dados enviados para criar um link encurtado.
 */
export interface LinkRequestModel {
  urlOriginal: string;
  alias?: string;
}