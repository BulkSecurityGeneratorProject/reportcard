(function() {
    'use strict';

    angular
        .module('reportcardApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('ecole', {
            parent: 'entity',
            url: '/ecole?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Ecoles'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/ecole/ecoles.html',
                    controller: 'EcoleController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
            }
        })
        .state('ecole-detail', {
            parent: 'entity',
            url: '/ecole/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Ecole'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/ecole/ecole-detail.html',
                    controller: 'EcoleDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Ecole', function($stateParams, Ecole) {
                    return Ecole.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'ecole',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('ecole-detail.edit', {
            parent: 'ecole-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/ecole/ecole-dialog.html',
                    controller: 'EcoleDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Ecole', function(Ecole) {
                            return Ecole.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('ecole.new', {
            parent: 'ecole',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/ecole/ecole-dialog.html',
                    controller: 'EcoleDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                nomfr: null,
                                noman: null,
                                devisefr: null,
                                devisean: null,
                                boitepostal: null,
                                logo: null,
                                logoContentType: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('ecole', null, { reload: 'ecole' });
                }, function() {
                    $state.go('ecole');
                });
            }]
        })
        .state('ecole.edit', {
            parent: 'ecole',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/ecole/ecole-dialog.html',
                    controller: 'EcoleDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Ecole', function(Ecole) {
                            return Ecole.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('ecole', null, { reload: 'ecole' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('ecole.delete', {
            parent: 'ecole',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/ecole/ecole-delete-dialog.html',
                    controller: 'EcoleDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Ecole', function(Ecole) {
                            return Ecole.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('ecole', null, { reload: 'ecole' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
