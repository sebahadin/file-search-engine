#!/bin/bash
# Clean script for file-search-engine project

echo "🧹 Cleaning build artifacts..."

# Remove all compiled .class files recursively
find . -name "*.class" -type f -delete

# Remove everything inside out/ if it exists
if [ -d "out" ]; then
  rm -rf out/*
  echo "✔ Cleared out/ directory"
fi

echo "✔ Removed all .class files"
echo "✅ Project cleaned successfully!"
