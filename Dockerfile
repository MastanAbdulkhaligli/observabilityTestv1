FROM eclipse-temurin:21-jre

WORKDIR /app

# Copy your fat jar
COPY build/libs/observability.jar /app/app.jar

# Copy the AspectJ weaver agent into the image
COPY docker/agents/aspectjweaver.jar /app/aspectjweaver.jar

EXPOSE 8080

# Run exactly like your host command, but inside container
ENTRYPOINT ["java", \
  "-Daj.weaving.verbose=true", \
  "-Dorg.aspectj.weaver.showWeaveInfo=true", \
  "-javaagent:/app/aspectjweaver.jar", \
  "-jar", "/app/app.jar" \
]
