export interface NamingTemplate {
  id: number;
  entityType: string;
  pattern: string;
  description: string;
  example: string;
  createdAt: string;
  updatedAt: string;
}

export interface CreateNamingTemplateRequest {
  entityType: string;
  pattern: string;
  description?: string;
  example?: string;
}
