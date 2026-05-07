@param String title
@param gg.jte.Content head
@param gg.jte.Content body
@param gg.jte.Content scripts

<!doctype html>
<html lang="de">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>${title}</title>

    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/@tabler/core@1.4.0/dist/css/tabler.min.css">
    <link rel="stylesheet" href="/css/app.css">

    @if(head != null)
        ${head}
    @endif
</head>

<body>
<script src="https://cdn.jsdelivr.net/npm/@tabler/core@1.4.0/dist/js/tabler-theme.min.js"></script>

<div class="page">

    <header class="navbar navbar-expand-md d-print-none">
        <div class="container-xl">

            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbar-menu">
                <span class="navbar-toggler-icon"></span>
            </button>

            <h1 class="navbar-brand navbar-brand-autodark d-none-navbar-horizontal pe-0 pe-md-3">
                <a href="/" class="text-decoration-none">
                    Minimal API
                </a>
            </h1>

            <div class="navbar-nav flex-row order-md-last">

                <div class="nav-item d-none d-md-flex me-3">
                    <a href="/settings" class="btn btn-outline-primary">
                        Settings
                    </a>
                </div>

                <div class="nav-item dropdown">
                    <a href="#" class="nav-link d-flex lh-1 text-reset p-0" data-bs-toggle="dropdown">
                        <span class="avatar avatar-sm">A</span>
                        <div class="d-none d-xl-block ps-2">
                            <div>Admin</div>
                            <div class="mt-1 small text-secondary">Administrator</div>
                        </div>
                    </a>

                    <div class="dropdown-menu dropdown-menu-end dropdown-menu-arrow">
                        <a href="/settings" class="dropdown-item">Settings</a>
                        <a href="#" class="dropdown-item">Profile</a>
                        <div class="dropdown-divider"></div>
                        <a href="#" class="dropdown-item text-danger">Logout</a>
                    </div>
                </div>

            </div>

            <div class="collapse navbar-collapse" id="navbar-menu">
                <div class="d-flex flex-column flex-md-row flex-fill align-items-stretch align-items-md-center">
                    <ul class="navbar-nav">

                        <li class="nav-item">
                            <a class="nav-link" href="/">
                                <span class="nav-link-icon d-md-none d-lg-inline-block">
                                    <svg xmlns="http://www.w3.org/2000/svg" class="icon" width="24" height="24"
                                         viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" fill="none"
                                         stroke-linecap="round" stroke-linejoin="round">
                                        <path stroke="none" d="M0 0h24v24H0z" fill="none"/>
                                        <path d="M5 12l-2 0l9 -9l9 9l-2 0"/>
                                        <path d="M5 12v7a2 2 0 0 0 2 2h10a2 2 0 0 0 2 -2v-7"/>
                                    </svg>
                                </span>
                                <span class="nav-link-title">Dashboard</span>
                            </a>
                        </li>

                        <li class="nav-item">
                            <a class="nav-link" href="/users">
                                <span class="nav-link-title">Users</span>
                            </a>
                        </li>

                        <li class="nav-item">
                            <a class="nav-link" href="/reports">
                                <span class="nav-link-title">Reports</span>
                            </a>
                        </li>

                        <li class="nav-item">
                            <a class="nav-link" href="/settings">
                                <span class="nav-link-title">Settings</span>
                            </a>
                        </li>

                    </ul>
                </div>
            </div>

        </div>
    </header>

    <div class="page-wrapper">

        <div class="page-header d-print-none">
            <div class="container-xl">
                <div class="row g-2 align-items-center">
                    <div class="col">
                        <div class="page-pretitle">Application</div>
                        <h2 class="page-title">${title}</h2>
                    </div>
                </div>
            </div>
        </div>

        <div class="page-body">
            <div class="container-xl">
                @if(body != null)
                    ${body}
                @endif
            </div>
        </div>

        <footer class="footer footer-transparent d-print-none">
            <div class="container-xl">
                <div class="row text-center align-items-center flex-row-reverse">
                    <div class="col-lg-auto ms-lg-auto">
                        <ul class="list-inline list-inline-dots mb-0">
                            <li class="list-inline-item">
                                <a href="/settings" class="link-secondary">Settings</a>
                            </li>
                            <li class="list-inline-item">
                                <a href="/health" class="link-secondary">Health</a>
                            </li>
                        </ul>
                    </div>
                    <div class="col-12 col-lg-auto mt-3 mt-lg-0">
                        <span class="text-secondary">
                            © 2026 Minimal API
                        </span>
                    </div>
                </div>
            </div>
        </footer>

    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/@tabler/core@1.4.0/dist/js/tabler.min.js"></script>
<script type="module" src="/js/app.js"></script>

@if(scripts != null)
    ${scripts}
@endif

</body>
</html>