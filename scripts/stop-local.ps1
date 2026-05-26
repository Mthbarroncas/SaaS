$root = Split-Path -Parent $PSScriptRoot
$projectMarkers = @(
    (Join-Path $root "services\risk-api"),
    (Join-Path $root "apps\web"),
    (Join-Path $root ".tools\apache-maven-3.9.9")
)

$processes = Get-CimInstance Win32_Process | Where-Object {
    $commandLine = $_.CommandLine
    $commandLine -and ($projectMarkers | Where-Object { $commandLine -like "*$_*" })
}

foreach ($process in $processes) {
    try {
        Stop-Process -Id $process.ProcessId -Force -ErrorAction Stop
    } catch {
    }
}

Write-Host "Processos locais encerrados."
