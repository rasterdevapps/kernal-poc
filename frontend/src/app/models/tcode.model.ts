export interface TCode {
  id: number;
  code: string;
  description: string;
  module: string;
  route: string;
  icon: string;
  active: boolean;
  createdAt: string;
  updatedAt: string;
}

export interface CreateTCodeRequest {
  code: string;
  description: string;
  module: string;
  route: string;
  icon?: string;
}
