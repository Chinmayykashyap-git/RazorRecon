import { Activity, AlertTriangle, CircleDot, GitBranch, LayoutDashboard, Search, WalletCards } from "lucide-react";
import { NavLink } from "react-router-dom";

const links = [
  { name: "Overview", path: "/", icon: LayoutDashboard },
  { name: "Reconciliation", path: "/reconciliation", icon: WalletCards },
  { name: "Investigations", path: "/investigations", icon: Search },
  { name: "Exceptions", path: "/exceptions", icon: AlertTriangle },
  { name: "Money Graph", path: "/graph", icon: GitBranch }
];

function Layout({ children }) {
  return (
    <div className="app-shell">
      <aside className="sidebar">
        <div className="brand">
          <div className="brand-mark"><CircleDot size={21} /></div>
          <div><div className="brand-name">RazorRecon</div><div className="brand-subtitle">Payment intelligence</div></div>
        </div>
        <div className="nav-label">Workspace</div>
        <nav className="sidebar-nav">
          {links.map(({ name, path, icon: Icon }) => (
            <NavLink key={path} to={path} className={({ isActive }) => `nav-item ${isActive ? "nav-item-active" : ""}`}>
              <Icon size={18} /><span>{name}</span>
            </NavLink>
          ))}
        </nav>
        <div className="system-status"><div className="status-dot" /><div><div className="status-title">System operational</div><div className="status-subtitle">All services healthy</div></div></div>
      </aside>
      <main className="main-content">
        <header className="topbar">
          <div><div className="topbar-title">RazorRecon</div><div className="topbar-subtitle">Payment reconciliation intelligence</div></div>
          <div className="live-indicator"><Activity size={15} /> LIVE</div>
        </header>
        <div className="page-content">{children}</div>
      </main>
    </div>
  );
}

export default Layout;
