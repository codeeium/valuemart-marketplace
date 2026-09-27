const { helloWorld } = require('./hello.js');

const test = require('node:test');
const assert = require('node:assert');

test('helloWorld returns "Hello, World!"', () => {
  assert.strictEqual(helloWorld(), 'Hello, World!');
});

test('helloWorld returns a string', () => {
  assert.strictEqual(typeof helloWorld(), 'string');
});

test('helloWorld returns not null', () => {
  assert.notStrictEqual(helloWorld(), null);
});

test('helloWorld returns not undefined', () => {
  assert.notStrictEqual(helloWorld(), undefined);
});

test('helloWorld returns not an empty string', () => {
  assert.notStrictEqual(helloWorld(), '');
});

test('helloWorld return value contains "Hello"', () => {
  assert.ok(helloWorld().includes('Hello'));
});

test('helloWorld return value contains "World"', () => {
  assert.ok(helloWorld().includes('World'));
});

test('helloWorld return value contains a comma', () => {
  assert.ok(helloWorld().includes(','));
});

test('helloWorld return value contains an exclamation mark', () => {
  assert.ok(helloWorld().includes('!'));
});

test('helloWorld return value has exact length of 13 characters', () => {
  assert.strictEqual(helloWorld().length, 13);
});
