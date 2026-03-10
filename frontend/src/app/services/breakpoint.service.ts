import { Injectable, signal, OnDestroy } from '@angular/core';

export type ScreenSize = 'mobile' | 'tablet' | 'desktop';

/**
 * Service that detects and tracks the current screen size breakpoint.
 * Uses standard responsive breakpoints:
 * - mobile: < 768px
 * - tablet: 768px - 1023px
 * - desktop: >= 1024px
 */
@Injectable({ providedIn: 'root' })
export class BreakpointService implements OnDestroy {
  private static readonly MOBILE_MAX = 767;
  private static readonly TABLET_MAX = 1023;

  readonly screenSize = signal<ScreenSize>(this.detectScreenSize());
  readonly isMobile = signal(this.detectScreenSize() === 'mobile');
  readonly isTablet = signal(this.detectScreenSize() === 'tablet');
  readonly isDesktop = signal(this.detectScreenSize() === 'desktop');

  private readonly resizeListener = (): void => this.onResize();

  constructor() {
    window.addEventListener('resize', this.resizeListener);
  }

  ngOnDestroy(): void {
    window.removeEventListener('resize', this.resizeListener);
  }

  private onResize(): void {
    const size = this.detectScreenSize();
    this.screenSize.set(size);
    this.isMobile.set(size === 'mobile');
    this.isTablet.set(size === 'tablet');
    this.isDesktop.set(size === 'desktop');
  }

  private detectScreenSize(): ScreenSize {
    const width = window.innerWidth;
    if (width <= BreakpointService.MOBILE_MAX) {
      return 'mobile';
    }
    if (width <= BreakpointService.TABLET_MAX) {
      return 'tablet';
    }
    return 'desktop';
  }
}
