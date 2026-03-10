import { Routes } from '@angular/router';
import { LayoutComponent } from './layout/layout.component';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { TCodeListComponent } from './pages/tcode/tcode-list/tcode-list.component';
import { ThemeListComponent } from './pages/theme/theme-list/theme-list.component';
import { ScreenListComponent } from './pages/screen/screen-list/screen-list.component';
import { NamingTemplateListComponent } from './pages/naming-template/naming-template-list/naming-template-list.component';
import { PreferencesComponent } from './pages/preferences/preferences.component';

export const routes: Routes = [
  {
    path: '',
    component: LayoutComponent,
    children: [
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
      { path: 'dashboard', component: DashboardComponent },
      { path: 'admin/tcodes', component: TCodeListComponent },
      { path: 'admin/themes', component: ThemeListComponent },
      { path: 'admin/screens', component: ScreenListComponent },
      { path: 'admin/naming-templates', component: NamingTemplateListComponent },
      { path: 'preferences', component: PreferencesComponent },
    ]
  },
  { path: '**', redirectTo: '/dashboard' }
];
