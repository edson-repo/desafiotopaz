
/**
 * Dados enviados para criar ou atualizar um link.
 */
export interface LinkRequestModel {
  urlOriginal: string;
  alias?: string | null;
}