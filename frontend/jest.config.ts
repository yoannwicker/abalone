import type { Config } from "jest";

const config: Config = {
  transform: {
    '^.+\\.(ts|html)$': ['ts-jest', {
      tsconfig: '<rootDir>/tsconfig.spec.json',
      stringifyContentPathRegex: '\\.html$',
    }],
  },
  coverageDirectory: "build/reports/front-coverage",
  reporters: [
    "default",
    ["jest-junit", { outputDirectory: "build/reports/front-tests", outputName: "jest-junit.xml" }]
  ]
};

export default config;
