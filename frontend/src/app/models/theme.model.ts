export interface Theme {
  id: number;
  themeName: string;
  description: string;
  primaryColor: string;
  secondaryColor: string;
  active: boolean;
  isDefault: boolean;
  createdAt: string;
  updatedAt: string;
}

export interface CreateThemeRequest {
  themeName: string;
  description?: string;
  primaryColor: string;
  secondaryColor: string;
  isDefault: boolean;
}
