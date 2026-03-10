export interface Screen {
  id: number;
  screenId: string;
  title: string;
  description: string;
  module: string;
  tcodeId: number;
  screenType: string;
  createdAt: string;
  updatedAt: string;
}

export interface CreateScreenRequest {
  screenId: string;
  title: string;
  description?: string;
  module: string;
  tcodeId?: number;
  screenType: string;
}
