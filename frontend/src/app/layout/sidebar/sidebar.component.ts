import { Component, signal } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';

interface NavItem {
  label: string;
  route: string;
  icon: string;
}

interface NavGroup {
  title: string;
  expanded: boolean;
  items: NavItem[];
}

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [RouterLink, RouterLinkActive],
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.scss'
})
export class SidebarComponent {
  readonly collapsed = signal(false);

  readonly favourites: NavItem[] = [
    { label: 'Dashboard', route: '/dashboard', icon: '📊' },
  ];

  readonly navGroups: NavGroup[] = [
    {
      title: 'Administration',
      expanded: true,
      items: [
        { label: 'T-Codes', route: '/admin/tcodes', icon: '⌨️' },
        { label: 'Themes', route: '/admin/themes', icon: '🎨' },
        { label: 'Screens', route: '/admin/screens', icon: '🖥️' },
        { label: 'Naming Templates', route: '/admin/naming-templates', icon: '📝' },
      ]
    },
    {
      title: 'User',
      expanded: true,
      items: [
        { label: 'Preferences', route: '/preferences', icon: '⚙️' },
      ]
    }
  ];

  toggleCollapse(): void {
    this.collapsed.set(!this.collapsed());
  }

  toggleGroup(group: NavGroup): void {
    group.expanded = !group.expanded;
  }
}
