#include <stdio.h>
#include <stdlib.h>
#include <conio.h>
#include <signal.h>
#include <unistd.h>

#define LOCK "fts.lock"

void init();
void run();
void onClose();
void handleSignal(int);
void holdScreen();

int main() {
    init();
    run();
    return 0;
}

void init() {
    // set window title
    system("title File Transfer System");

    // allow only one instance
    if (access(LOCK, F_OK) != -1) {
        // display error message
        printf("Another instance is already running!\nOnly one instance is allowed.");

        // hold the screen
        holdScreen();

        // return exit code 1
        exit(1);
    } else {
        // create lock
        FILE *fptr = fopen(LOCK, "w");
        fclose(fptr);
    }

    // set up signals
    signal(SIGINT, handleSignal);
    signal(SIGBREAK, handleSignal);
}

void run() {
    // display IP address
    printf("--------------------------------------------------------------------------------\n");
    system("ipconfig | findstr IPv4");
    printf("--------------------------------------------------------------------------------\n\n");

    // execute application
    system("java-runtime\\bin\\java -jar fts-0.0.1-SNAPSHOT.war");

    // hold the screen
    holdScreen();
}

void onClose() {
    // remove the lock
    remove(LOCK);
}

void handleSignal(int signal) {
    // if interrupted, start closing event
    onClose();
}

void holdScreen() {
    // display message and wait for any key
    printf("\n\nPress any key to exit ...");
    getch();
}
