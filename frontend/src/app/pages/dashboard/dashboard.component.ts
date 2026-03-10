import { Component, OnInit, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { TCodeService } from '../../services/tcode.service';
import { TCode } from '../../models/tcode.model';

interface QuickTile {
  label: string;
  route: string;
  icon: string;
  description: string;
}

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss'
})
export class DashboardComponent implements OnInit {
  private readonly tcodeService = inject(TCodeService);

  readonly tcodes = signal<TCode[]>([]);
  readonly loading = signal(true);

  readonly quickTiles: QuickTile[] = [
    { label: 'T-Codes', route: '/admin/tcodes', icon: '⌨️', description: 'Manage transaction codes' },
    { label: 'Themes', route: '/admin/themes', icon: '🎨', description: 'Customize appearance' },
    { label: 'Screens', route: '/admin/screens', icon: '🖥️', description: 'Screen configuration' },
    { label: 'Templates', route: '/admin/naming-templates', icon: '📝', description: 'Naming templates' },
    { label: 'Preferences', route: '/preferences', icon: '⚙️', description: 'User settings' },
  ];

  ngOnInit(): void {
    this.loadActiveTCodes();
  }

  private loadActiveTCodes(): void {
    this.tcodeService.findAllActive().subscribe({
      next: (tcodes) => {
        this.tcodes.set(tcodes.slice(0, 10));
        this.loading.set(false);
      },
      error: (err) => {
        console.error('Failed to load active T-Codes from API:', err.message, err);
        this.loading.set(false);
      }
    });
  }
}
