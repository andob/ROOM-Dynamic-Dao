set -o allexport

echo "Publishing..."

./gradlew :dyndao:publishToMavenLocal
