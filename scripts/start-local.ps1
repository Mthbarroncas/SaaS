$ErrorActionPreference = "Stop"

$root = Split-Path -Parent $PSScriptRoot
$mavenHome = Join-Path $root ".tools\apache-maven-3.9.9"
$mavenCmd = Join-Path $mavenHome "bin\mvn.cmd"

if (-not (Test-Path $mavenCmd)) {
    Write-Host "Baixando Maven local..."
    New-Item -ItemType Directory -Force (Join-Path $root ".tools") | Out-Null
    $zipPath = Join-Path $root ".tools\apache-maven-3.9.9-bin.zip"
    Invoke-WebRequest -Uri "https://archive.apache.org/dist/maven/maven-3/3.9.9/binaries/apache-maven-3.9.9-bin.zip" -OutFile $zipPath
    Expand-Archive -LiteralPath $zipPath -DestinationPath (Join-Path $root ".tools") -Force
}

$projectMarkers = @(
    (Join-Path $root "services\risk-api"),
    (Join-Path $root "apps\web"),
    $mavenHome
)

Get-CimInstance Win32_Process | Where-Object {
    $commandLine = $_.CommandLine
    $commandLine -and ($projectMarkers | Where-Object { $commandLine -like "*$_*" })
} | ForEach-Object {
    try {
        Stop-Process -Id $_.ProcessId -Force -ErrorAction Stop
    } catch {
    }
}

Remove-Item (Join-Path $root "services\risk-api\target\fraudshield-local.mv.db") -ErrorAction SilentlyContinue
Remove-Item (Join-Path $root "services\risk-api\target\fraudshield-local.trace.db") -ErrorAction SilentlyContinue

Write-Host "Instalando dependencias do frontend..."
Push-Location (Join-Path $root "apps\web")
cmd /c npm install
Pop-Location

$backendOut = Join-Path $root "services\risk-api\target\risk-api-local.out.log"
$backendErr = Join-Path $root "services\risk-api\target\risk-api-local.err.log"
$frontendOut = Join-Path $root "apps\web\.next-dev.out.log"
$frontendErr = Join-Path $root "apps\web\.next-dev.err.log"

Write-Host "Iniciando backend local em http://localhost:8080 ..."
Start-Process -FilePath $mavenCmd `
    -ArgumentList "spring-boot:run -Dspring-boot.run.profiles=local" `
    -WorkingDirectory (Join-Path $root "services\risk-api") `
    -RedirectStandardOutput $backendOut `
    -RedirectStandardError $backendErr `
    -WindowStyle Hidden

Write-Host "Iniciando frontend local em http://localhost:3000 ..."
Start-Process -FilePath "cmd.exe" `
    -ArgumentList "/c", "npm run dev" `
    -WorkingDirectory (Join-Path $root "apps\web") `
    -RedirectStandardOutput $frontendOut `
    -RedirectStandardError $frontendErr `
    -WindowStyle Hidden

for ($i = 0; $i -lt 30; $i++) {
    try {
        $backendReady = (Test-NetConnection -ComputerName localhost -Port 8080 -WarningAction SilentlyContinue).TcpTestSucceeded
        $frontendReady = (Test-NetConnection -ComputerName localhost -Port 3000 -WarningAction SilentlyContinue).TcpTestSucceeded
        if ($backendReady -and $frontendReady) {
            break
        }
    } catch {
    }
    Start-Sleep -Seconds 2
}

Write-Host "Ambiente iniciado."
Write-Host "Backend: http://localhost:8080"
Write-Host "Frontend: http://localhost:3000"
