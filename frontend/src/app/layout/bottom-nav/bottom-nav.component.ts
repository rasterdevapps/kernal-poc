import { Component, inject } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { BreakpointService } from '../../services/breakpoint.service';

interface BottomNavItem {
  label: string;
  route: string;
  icon: string;
}

@Component({
  selector: 'app-bottom-nav',
  standalone: true,
  imports: [RouterLink, RouterLinkActive],
  templateUrl: './bottom-nav.component.html',
  styleUrl: './bottom-nav.component.scss'
})
export class BottomNavComponent {
  readonly breakpoint = inject(BreakpointService);

  readonly navItems: BottomNavItem[] = [
    { label: 'Home', route: '/dashboard', icon: '🏠' },
    { label: 'T-Codes', route: '/admin/tcodes', icon: '⌨️' },
    { label: 'Screens', route: '/admin/screens', icon: '🖥️' },
    { label: 'Settings', route: '/preferences', icon: '⚙️' },
  ];
}
