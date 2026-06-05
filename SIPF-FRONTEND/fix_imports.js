const fs = require('fs');
const path = require('path');

function walk(dir) {
    let results = [];
    const list = fs.readdirSync(dir);
    list.forEach(function(file) {
        file = path.join(dir, file);
        const stat = fs.statSync(file);
        if (stat && stat.isDirectory()) { 
            results = results.concat(walk(file));
        } else { 
            if (file.endsWith('.ts')) results.push(file);
        }
    });
    return results;
}

const files = walk('./src/app');

files.forEach(file => {
    let content = fs.readFileSync(file, 'utf8');
    let changed = false;

    // A mapping of old relative patterns to new patterns, based on the file's current location vs root
    // To keep it simple, we replace occurrences of generic old paths with new paths if we find them.

    // core/services -> core/use-cases
    if (content.includes('/core/services/')) {
        content = content.replace(/\/core\/services\//g, '/core/use-cases/');
        changed = true;
    }
    // core/models -> core/entities
    if (content.includes('/core/models/')) {
        content = content.replace(/\/core\/models\//g, '/core/entities/');
        changed = true;
    }
    // core/guards -> core/ports
    if (content.includes('/core/guards/')) {
        content = content.replace(/\/core\/guards\//g, '/core/ports/');
        changed = true;
    }
    // core/interceptors -> core/ports
    if (content.includes('/core/interceptors/')) {
        content = content.replace(/\/core\/interceptors\//g, '/core/ports/');
        changed = true;
    }

    // Special replacements for app.routes.ts, app.config.ts which moved into main/
    if (file.includes('app.routes.ts') || file.includes('app.config.ts')) {
        // they were at root, now in main/. So './core/...' becomes '../core/...'
        if (content.includes('./core/')) {
            content = content.replace(/\.\/core\//g, '../core/');
            changed = true;
        }
        // './pages/login/login' -> '../features/auth/login/login'
        if (content.includes('./pages/login/')) {
            content = content.replace(/\.\/pages\/login\//g, '../features/auth/login/');
            changed = true;
        }
        if (content.includes('./pages/recuperar-contrasena/')) {
            content = content.replace(/\.\/pages\/recuperar-contrasena\//g, '../features/auth/recuperar-contrasena/');
            changed = true;
        }
        if (content.includes('./pages/restablecer-contrasena/')) {
            content = content.replace(/\.\/pages\/restablecer-contrasena\//g, '../features/auth/restablecer-contrasena/');
            changed = true;
        }
        if (content.includes('./pages/unauthorized/')) {
            content = content.replace(/\.\/pages\/unauthorized\//g, '../features/auth/unauthorized/');
            changed = true;
        }
        if (content.includes('./pages/admin/')) {
            content = content.replace(/\.\/pages\/admin\//g, '../features/admin/');
            changed = true;
        }
        if (content.includes('./pages/instructor/')) {
            content = content.replace(/\.\/pages\/instructor\//g, '../features/instructor/');
            changed = true;
        }
        if (content.includes('./pages/coordinador/')) {
            content = content.replace(/\.\/pages\/coordinador\//g, '../features/coordinador/');
            changed = true;
        }
    }

    // Update references to app layout or components if necessary
    // 'src/app/shared/components' hasn't changed relative depth to features if features replace pages
    // Pages were in src/app/pages/X/, Features are in src/app/features/X/ or src/app/features/auth/X/
    // Depth for auth: src/app/pages/login/ -> src/app/features/auth/login/
    // Wait, that's one directory deeper!
    // So imports from `../../core/...` in login.ts now need to be `../../../core/...`
    if (file.includes('features/auth/')) {
        // we moved from pages/login/ (depth 2) to features/auth/login/ (depth 3)
        // so `../../` should become `../../../`
        // we'll replace `../../core` with `../../../core` and `../../shared` with `../../../shared`
        const authContentOld = content;
        content = content.replace(/\.\.\/\.\.\/core/g, '../../../core');
        content = content.replace(/\.\.\/\.\.\/shared/g, '../../../shared');
        if (authContentOld !== content) changed = true;
    }

    if (changed) {
        fs.writeFileSync(file, content, 'utf8');
        console.log('Fixed imports in', file);
    }
});
