import { Component, inject, signal } from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';
import { HeaderComponent } from './header/header.component';
import { SidebarComponent } from './sidebar/sidebar.component';
import { FooterComponent } from './footer/footer.component';
import { BottomNavComponent } from './bottom-nav/bottom-nav.component';
import { BreakpointService } from '../services/breakpoint.service';

@Component({
  selector: 'app-layout',
  standalone: true,
  imports: [RouterOutlet, HeaderComponent, SidebarComponent, FooterComponent, BottomNavComponent],
  templateUrl: './layout.component.html',
  styleUrl: './layout.component.scss'
})
export class LayoutComponent {
  private readonly router = inject(Router);
  readonly breakpoint = inject(BreakpointService);
  readonly mobileSidebarOpen = signal(false);

  onTCodeSubmitted(code: string): void {
    this.router.navigate(['/dashboard'], { queryParams: { tcode: code } });
    if (this.breakpoint.isMobile() || this.breakpoint.isTablet()) {
      this.mobileSidebarOpen.set(false);
    }
  }

  toggleMobileSidebar(): void {
    this.mobileSidebarOpen.set(!this.mobileSidebarOpen());
  }

  closeMobileSidebar(): void {
    this.mobileSidebarOpen.set(false);
  }
}
